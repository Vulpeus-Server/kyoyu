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
        if os.getenv('BUILD_RELEASE') != 'true':
            buildNumber = os.getenv('BUILD_ID')
            if buildNumber:
                output_path += f'+build.{buildNumber}'
            else:
                output_path += '-SNAPSHOT'
        output_path += '.jar'

        with zipfile.ZipFile(output_path, 'w',
                compression=zipfile.ZIP_DEFLATED,
                compresslevel=9) as outfile:
            manifest: dict = {}
            for jar_path in jars:
                with zipfile.ZipFile(jar_path, 'r') as infile:
                    for file in infile.namelist():
                        if file == 'META-INF/MANIFEST.MF':
                            with infile.open(file, 'r') as rf:
                                for line in rf.read().decode('utf-8').splitlines():
                                    if ':' in line:
                                        k, v = line.split(':', 1)
                                        manifest[k] = v
                            continue
                        if file in outfile.namelist():
                            continue
                        with outfile.open(file, 'w') as wf:
                            with infile.open(file, 'r') as rf:
                                wf.write(rf.read())

            with outfile.open('META-INF/MANIFEST.MF', 'w') as wf:
                manifest_content = ""
                for key, value in manifest.items():
                    manifest_content += f'{key}: {value}\r\n'
                wf.write(manifest_content.encode('utf-8'))

        print(output_path)



if __name__ == '__main__':
    main()
