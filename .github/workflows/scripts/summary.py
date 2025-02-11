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

import jproperties


def read_prop(file_name: str, key: str) -> str:
	configs = jproperties.Properties()
	with open(file_name, 'rb') as f:
		configs.load(f)
	return configs[key].data


def get_sha256_hash(file_path: str) -> str:
	sha256_hash = hashlib.sha256()

	with open(file_path, 'rb') as f:
		for buf in iter(functools.partial(f.read, 4096), b''):
			sha256_hash.update(buf)

	return sha256_hash.hexdigest()


def main():
	target_version_env = os.environ.get('TARGET_VERSION', '')
	target_versions = list(filter(None, target_version_env.split(',') if target_version_env != '' else []))
	print('target_versions: {}'.format(target_versions))

	with open('settings.json') as f:
		settings: dict = json.load(f)

	with open(os.environ['GITHUB_STEP_SUMMARY'], 'w') as f:
		f.write('## Build Artifacts Summary\n\n')
		f.write('| Subproject | for Minecraft | File | Size | SHA-256 |\n')
		f.write('| --- | --- | --- | --- | --- |\n')

		warnings = []
		for version in settings['versions']:
			if len(target_versions) > 0 and version['version'] not in target_versions:
				print('skipping {}'.format(version['version']))
				continue
			game_versions = version['game_versions']
			game_versions = game_versions.strip().replace('\r', '').replace('\n', ', ')
			file_paths = glob.glob('build-artifacts/build/libs/*.jar')
			file_paths = list(filter(lambda fp: not fp.endswith('-sources.jar') and not fp.endswith('-dev.jar') and not fp.endswith('-shadow.jar'), file_paths))
			if len(file_paths) == 0:
				file_name = '*not found*'
				sha256 = '*N/A*'
			else:
				file_name = '`{}`'.format(os.path.basename(file_paths[0]))
				file_size = '{} B'.format(os.path.getsize(file_paths[0]))
				sha256 = '`{}`'.format(get_sha256_hash(file_paths[0]))
				if len(file_paths) > 1:
					warnings.append('Found too many build files in subproject {}: {}'.format(version, ', '.join(file_paths)))

			f.write('| {} | {} | {} | {} | {} |\n'.format(version, game_versions, file_name, file_size, sha256))

		if len(warnings) > 0:
			f.write('\n### Warnings\n\n')
			for warning in warnings:
				f.write('- {}\n'.format(warning))


if __name__ == '__main__':
	main()