"""
Update Modrinth Readme
"""
__author__ = 'topi_banana'

import os
import sys
import json
import requests
from rich import print

def throw_api(id: str, body: str = "") -> int:

  res = requests.patch(
    f"https://api.modrinth.com/v2/project/{id}",
    data=json.dumps({
      "body": body
    }),
    headers={
      "Content-Type": "application/json",
      "Authorization": os.environ['SECRET'],
    },
  )

  if res.status_code != 204:
    body = res.json()
    print(body["error"])
    print(body["description"])
    sys.exit(1)

def main(id: str, path: str):
  with open(path, encoding="utf_8") as f:
    body = f.read()
  throw_api(id, body)

if __name__ == '__main__':
  args = sys.argv
  main(id=args[1], path=args[2])