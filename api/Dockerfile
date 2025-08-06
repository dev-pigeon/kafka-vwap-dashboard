# get python image
FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt

# copy rest of the server logic
COPY . . 

# expose the port Flask will run on
EXPOSE 5335

# run the app
CMD ["python", "api.py"]

