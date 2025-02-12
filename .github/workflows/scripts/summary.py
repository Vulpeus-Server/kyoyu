"""
A script to scan through all valid mod jars in build-artifacts.zip/$version/build/libs,
and generate an artifact summary table for that to GitHub action step summary
"""
__author__ = 'Fallen_Breath and topi_banana'

import functools
import glob
import hashlib
import json
import os

def get_sha256_hash(file_path: str) -> str:
	sha256_hash = hashlib.sha256()

	with open(file_path, 'rb') as f:
		for buf in iter(functools.partial(f.read, 4096), b''):
			sha256_hash.update(buf)

	return sha256_hash.hexdigest()


def main():
	with open('settings.json') as f:
		settings: dict = json.load(f)

	with open(os.environ['GITHUB_STEP_SUMMARY'], 'w') as f:
		f.write('## Build Artifacts Summary\n\n')
		f.write('| File | Platform | for Minecraft | Size | SHA-256 |\n')
		f.write('| --- | --- | --- | --- | --- |\n')

		warnings = []
		for version in settings['versions']:
			game_versions = version['game_versions'].strip().replace('\r', '').replace('\n', ', ')
			platforms = version['platforms'].strip().replace('\r', '').replace('\n', ', ')
			file_path = f'build-artifacts/*-mc{version["version"]}-*.jar'
			print(file_path)
			file_paths = glob.glob(file_path)
			print(file_paths)
			if len(file_paths) == 0:
				file_name = '*not found*'
				file_size = '*N/A*'
				sha256 = '*N/A*'
			else:
				file_name = '`{}`'.format(os.path.basename(file_paths[0]))
				file_size = '{} B'.format(os.path.getsize(file_paths[0]))
				sha256 = '`{}`'.format(get_sha256_hash(file_paths[0]))
				if len(file_paths) > 1:
					warnings.append('Found too many build files in subproject {}: {}'.format(version['version'], ', '.join(file_paths)))

			f.write('| {} | {} | {} | {} | {} |\n'.format(file_name, platforms, game_versions, file_size, sha256))

		if len(warnings) > 0:
			f.write('\n### Warnings\n\n')
			for warning in warnings:
				f.write('- {}\n'.format(warning))


if __name__ == '__main__':
	main()