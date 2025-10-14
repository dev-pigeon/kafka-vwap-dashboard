# Client

### Role in the System

This directory contains a React-based frontend. The frontend receives the VWAP scores every second via the Flask-based websocket.

## Contents

- `main.tsx` - Entry point
- `App.tsx` - Outer container that holds sub components
- `Header.tsx` - A simple header to display app name and last-updated information
- `VwapList.tsx` - A horizontal bar graph that lists the current top five stocks by VWAP

## Setup & Run

### Local

```bash
npm install
npm run dev
```

### Containerized

```bash
docker-compose up --build -d client
```

**Watch out! ⚠️ :** This command must be run from the root directory of the project
