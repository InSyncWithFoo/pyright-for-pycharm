# Common configurations


## Executable

For CLI/LSP to work, at least one executable file needs to be defined
using either the <b>Global</b> or <b>Project</b> panel.

Such a file is typically named `pyright`/`pyright-python` (CLI)
or `pyright-langserver`/`pyright-python-langserver` (LSP)
and can likely be found in:

??? question "What's the difference between these files?"

    [TLDR][1]: Some may output "unexpected" things.

| Manager | Type   | OS      | Directory                                          |
|---------|:-------|:--------|:---------------------------------------------------|
| NPM     | Global | Windows | `%APPDATA%\npm`                                    |
| NPM     | Global | Linux   | `/usr/local/bin`                                   |
| Pip     | Global | Windows | `%LOCALAPPDATA%\Programs\Python\<version>\Scripts` |
| Pip     | Global | Linux   | `~/.local/bin`                                     |
| NPM     | Local  | Windows | `.\node_modules\.bin`                              |
| NPM     | Local  | Linux   | `./node_modules/.bin`                              |
| Pip     | Local  | Windows | `.\<your-venv>\Scripts`                            |
| Pip     | Local  | Linux   | `./<your-venv>/bin`                                |

If the executables can't be found in the aforementioned locations,
see the following pages for more information:

* <i>[Where does npm install packages?][2]</i> - <i>Stack Overflow</i>
* <i>[Where does pip install its packages?][3]</i> - <i>Stack Overflow</i>
* <i>[folders][4]</i> - <i>npm Docs</i>

You can also use a relative path.
It would be interpreted as relative to the project directory.

The executable is used as-is with no additional checks,
so CLI/LSP will still work even if, for example,
it's a wrapper script that outputs diagnostics in the expected format.


## Always use global

Check this option to always use the global executable
and configuration file.

Default: `false`


## Use editor font

Check this option to display annotations in the editor font.
This option is not applied retroactively;
you might need to make an edit to see the effect.

Default: `false`


## Add prefix to tooltips

Check this option to prefix tooltips with "Pyright:".
This option is not applied retroactively;
you might need to make an edit to see the effect.

Default: `false`


## Auto-suggest executable

Check this option to automatically find and
suggest an executable for the current project on open.
See [the corresponding feature][5] for more information.

Default: `true`


  [1]: ../faq.md#whats-the-difference-between-the-pyright-and-pyright-python-files
  [2]: https://stackoverflow.com/q/5926672
  [3]: https://stackoverflow.com/q/29980798
  [4]: https://docs.npmjs.com/cli/v10/configuring-npm/folders#executables
  [5]: ../features.md#executable-suggestion
