# Frequently asked questions


## My code should have no/these errors, but it does/doesn't.

This plugin contains no type checking logic.
For type checking bugs, please report them to [the Pyright issue tracker][1].


## What's the difference between the `pyright` and `pyright-python` files?

Pyright is written in TypeScript and officially published on NPM,
mainly for the purpose of better integration with VSCode.
This requires a dependency on Node, which Python developers
might not have on their development machines.

[The PyPI `pyright` package][2] was created to solve this problem.
When installed, it places 4 proxy executables in
the same virtual environment/directory you have your `pip` in:

* `pyright`/`pyright-langserver` (these have the same names as the originals)
* `pyright-python`/`pyright-python-langserver`

These proxies will automatically install the actual NPM package
if it is not already installed, then re-output the results of
the original executables. [With the correct configurations][3],
new versions may be automatically installed at runtime.

If the corresponding version of the NPM package has not been installed,
the proxies will also re-output the "added 1 package" notice by NPM,
which <em>will</em> cause parsing errors.
Due to this, it is recommended that the original executables are used instead.

Said original executables can typically be found at:

* Windows: `%HOMEPATH%/.cache/pyright-python/<version>/node_modules/.bin`
* Linux: `~/.cache/pyright-python/<version>/node_modules/.bin`


## Why does the plugin have to perform saves so often in command line mode?

Pyright only reads actual files on disk.
It does not support passing files from stdin.
[A feature request][4] was made and quickly rejected.

Adding an option that makes the plugin run only on "manual" saves
(the *Save All* action) is counter-productive, since that doesn't
guarantee the annotator class is called. This is [a known limitation][5].

Use [the LSP4IJ mode][6] instead.


## Is the command-line watch mode (`--watch`) supported?

No. Use LSP4IJ mode instead.


## What does this command line option do?

Refer to [Pyright's documentation][7] for the meaning of these options.


## Is this plugin affiliated with Microsoft/JetBrains?

No, or at least not in a business or ownership sense.

It was, however, created out of adoration of Pyright and JetBrains IDEs.


## I love this project. How can I support it?

You can consider [sponsoring it][8].

If you are feeling generous, see [`CONTRIBUTING.md`][9]
for how to contribute non-financially.


  [1]: https://github.com/microsoft/pyright/issues
  [2]: https://pypi.org/project/pyright/
  [3]: https://github.com/RobertCraigie/pyright-python/blob/HEAD/README.md#automatically-keeping-pyright-up-to-date
  [4]: https://github.com/microsoft/pyright/issues/7282
  [5]: https://github.com/InSyncWithFoo/pyright-for-pycharm/issues/10
  [6]: configurations/running-modes.md
  [7]: https://microsoft.github.io/pyright/#/command-line
  [8]: https://github.com/sponsors/InSyncWithFoo
  [9]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/master/CONTRIBUTING.md
