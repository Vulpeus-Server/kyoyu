__author__ = 'topi_banana'

import json
import glob
import os
import sys
import zipfile

def main():
    with open('settings.json') as f:
        settings: dict = json.load(f)

    for version in settings['versions']:

        jars = []
        mc_ver = version['version']
        platforms = version['platforms'].split('\n')
        for platform in platforms:
            subproject = f'{platform}-{mc_ver}'
            jars.extend(glob.glob(f'versions/{subproject}/build/libs/*-relocate.jar'))

        if len(jars) == 0:
            sys.exit(1)

        output_path = os.getenv("FILE_SUFFIX")
        output_path += f'-mc{mc_ver}'
        if os.getenv('GITHUB_EVENT_NAME') != 'release':
            buildNumber = os.getenv('GITHUB_RUN_ID')
            if buildNumber:
                output_path += f'+build.{buildNumber}'
            else:
                output_path += '-SNAPSHOT'
        output_path += '.jar'

        with zipfile.ZipFile(output_path, 'w',
                compression=zipfile.ZIP_DEFLATED,
                compresslevel=9) as outfile:
            for jar_path in jars:
                with zipfile.ZipFile(jar_path, 'r') as infile:
                    for file in infile.namelist():
                        if file in outfile.namelist():
                            continue
                        with outfile.open(file, 'w') as wf:
                            with infile.open(file, 'r') as rf:
                                wf.write(rf.read())

        print(output_path)



if __name__ == '__main__':
    main()
