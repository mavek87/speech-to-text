# SPEECH-TO-TEXT SYSTEM

## Architecture

- Backend
  - REST API (Java & Javalin)
  - Speech to text converter (Python & [OpenAI Whisper](https://github.com/openai/whisper)):
- Frontend (React & Typescript)

## Prerequisites:

1. JVM Java 17
2. Python3
3. Whisper:

`pip install -U openai-whisper`

Alternatively, the following command will pull and install the latest commit from this repository, along with its Python dependencies:

`pip install git+https://github.com/openai/whisper.git`
To update the package to the latest version of this repository, please run:

`pip install --upgrade --no-deps --force-reinstall git+https://github.com/openai/whisper.git`

It also requires the command-line tool ffmpeg to be installed on your system, which is available from most package managers:

#### on Ubuntu or Debian
sudo apt update && sudo apt install ffmpeg

#### Other doc on Whisper
- https://huggingface.co/openai/whisper-medium