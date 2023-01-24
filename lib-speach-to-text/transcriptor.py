import json
import socket
from concurrent.futures import ThreadPoolExecutor
import whisper

def handle_request(conn, model):
    data = conn.recv(4096)
    request = json.loads(data.decode())
    callback_address = request.get('callback_address', None)
    callback_port = request.get('callback_port', None)
    id = request.get('id', None)
    signal = request.get('signal', None)
    file_name = request.get('file_name', None)

    if callback_address is None or callback_port is None:
        conn.close()
        return
    elif id is None:
        response = {'status_code': 400, 'message': 'id is required'}
    elif file_name is None:
        response = {'status_code': 400, 'message': 'file_name is required', 'id': id}
    elif signal == "TRANSCRIBE_AUDIO":
        result = model.transcribe(file_name)
        response = {'status_code': 200, 'message': (result["text"]), 'id': id}
    else:
        response = {'status_code': 400, 'message': 'Invalid signal', 'id': id}

    send_json_through_socket(callback_address, callback_port, response)
    conn.close()

def send_json_through_socket(host, port, json_data):
    # Create a socket object
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # Connect to the specified host and port
    s.connect((host, port))
    # Encode the data as a JSON string
    data = json.dumps(json_data)
    # Send the data
    s.sendall(data.encode())
    # Close the connection
    s.close()

def start_server():
    model = whisper.load_model("small")
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.bind(('localhost', 8080))
    server.listen()

    # Create a thread pool with a maximum number of threads
    with ThreadPoolExecutor(max_workers=100) as executor:
        while True:
            conn, addr = server.accept()
            print(f'Connected by {addr}')
            executor.submit(handle_request, conn, model)

if __name__ == '__main__':
    start_server()