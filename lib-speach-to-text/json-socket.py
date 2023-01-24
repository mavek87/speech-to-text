import json
import socket

def receive_json(host, port):
    # Create a socket object
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # Bind the socket to a specific address and port
    s.bind((host, port))
    # Listen for incoming connections
    s.listen()
    print("Listening for incoming connections...")
    # Accept a connection
    conn, addr = s.accept()
    print(f"Connection from {addr} has been established.")
    # Receive JSON data
    data = conn.recv(4096)
    # Decode the data and convert it to a Python object
    json_data = json.loads(data.decode())
    # Close the connection
    conn.close()
    return json_data

def send_json(host, port, json_data):
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