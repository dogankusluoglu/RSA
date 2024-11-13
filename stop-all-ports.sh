#!/bin/bash

# exammple usage
# ./stop-all-ports.sh 7000 5713

# Function to kill process listening on a given port
kill_process_on_port() {
  local port=$1
  local pid=$(lsof -t -i:$port)
  if [ -z "$pid" ]; then
    echo "No process listening on port $port"
  else
    echo "Killing process $pid on port $port"
    kill -9 $pid
  fi
}

# Default ports to check
default_ports=(7000 5713)

# Kill processes on default ports
for port in "${default_ports[@]}"; do
  kill_process_on_port $port
done

# Kill processes on additional ports provided as arguments
if [ $# -gt 0 ]; then
  for port in "$@"; do
    kill_process_on_port $port
  done
fi