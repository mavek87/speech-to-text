import sys
import whisper

mod = sys.argv[1]
if mod is None:
    mod = "base"

filename = sys.argv[2]
lang = sys.argv[3]
verbose = sys.argv[4]

model = whisper.load_model(mod)

if lang is None:
    result = model.transcribe(filename)
else:
    result = model.transcribe(filename,language=lang)

if verbose is True or verbose == "True":
    print(result)
else:
    print(result["text"])
