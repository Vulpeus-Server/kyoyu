"""
A script to scan through the versions directory and collect all folder names as the subproject list,
then output a json as the github action include matrix
"""
__author__ = 'Fallen_Breath and topi_banana'

import json
import os
import sys


def main():
	target_version_env = os.environ.get('TARGET_VERSION', '')
	target_versions = list(filter(None, target_version_env.split(',') if target_version_env != '' else []))
	print('target_versions: {}'.format(target_versions))

	with open('settings.json') as f:
		settings: dict = json.load(f)

	if len(target_versions) == 0:
		versions = settings['versions']
	else:
		versions = []
		for version in settings['versions']:
			if version['version'] in target_versions:
				versions.append(version)
				target_versions.remove(version['version'])
		if len(target_versions) > 0:
			print('Unexpected subprojects: {}'.format(target_versions), file=sys.stderr)
			sys.exit(1)

	matrix_entries = []
	for version in versions:
		matrix_entries.append(version)
	matrix = {'include': matrix_entries}
	with open(os.environ['GITHUB_OUTPUT'], 'w') as f:
		f.write('matrix={}\n'.format(json.dumps(matrix)))

	print('matrix:')
	print(json.dumps(matrix, indent=2))


if __name__ == '__main__':
	main()
