import whisper

filename = sys.argv[0]

model = whisper.load_model("small")
result = model.transcribe(filename)
print(result["text"])