# Bin

### Role in the System

This directory contains various files and scripts required to initialize the project. Its purpose is to facilitate the demo process by automatically downloading and cleaning the data required to run the application.

## Contents

- `clean_data.py` – Python script to clean / engineer dataset (used internally by `setup.sh``)
- `clean_postgre.sql` - Simple dev script to clear PostgreSQL table
- `requirements.txt` - Python dependencies
- `reset.sh` - Dev script to reset Kafka environment and db instance
- `run.sh` - Deb script to run Kafka locally
- `setup.sh` - Script to download and clean dataset

## Setup & Run

**Watch out! ⚠️ :** Make sure to run the command `chmod +x [file_name]` for all shell scripts.

### setup.sh

Before running the setup script, create a virtual environment and install the necessary dependencies:

```bash
python3 -m venv venv
source venv/bin/activate
pip3 install -r requirements.txt
./setup.sh
```

Then run:

```bash
./setup.sh
```

**Note:** `setup.sh` internally calls `clean_data.py` to clean/engineer the dataset. `clean_data.py` is not meant to be run standalone.

### reset.sh

```bash
./reset.sh
```

### run.sh

```bash
./run.sh [class_name]
```

- **Example:** `./run.sh StreamProducer`

## Notes

The `setup.sh` script relies on the original directory structure being intact. In order for the script to place the cleaned dataset in the correct location, please maintain the original directory structure as it was when the repository was cloned.
