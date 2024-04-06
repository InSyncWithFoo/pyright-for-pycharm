# Frequently asked questions


## What exactly is this plugin doing?

In a nutshell:

* You edit your code.
* This plugin saves your (unsaved) files.
* It then invokes the executable(s) provided by you
  with some hardcoded arguments.
* The results of which are used to show annotations.

And that's it.

It expects two things:

* That the executable you provide accepts said arguments, and
* that it outputs diagnostics in the formats specified [here][1].

As long as you respect these <em>requirements</em>,
you can use your own script, or whatever.
If you don't, expect some big bad error messages.


## My code should have no/these errors, but it does/doesn't.

This plugin contains no type checking logic.
For type checking bugs, please report them to [the Pyright issue tracker][2].


## What's the difference between `pyright` and `pyright-python`?

Pyright is written in TypeScript and published on NPM,
mainly for the purpose of better integration with VSCode.
This requires a dependency on Node, which Python developers
might not have on their development machines.

[The `pyright` package on PyPI][3] was created to solve this problem.
It will automatically install the actual NPM package and
places 4 proxy executables in the same virtual environment/directory
you have your `pip` in:

* `pyright`/`pyright-langserver` (these have the same names as the originals)
* `pyright-python`/`pyright-python-langserver`

These proxies will re-output the results of the original executables.
[With the correct configurations][4],
new versions can be automatically installed at runtime.

If you use this package, the original executables can typically be found at:

* Windows: `%HOMEPATH%/.cache/pyright-python/<version>/node_modules/.bin`
* Linux: `~/.cache/pyright-python/<version>/node_modules/.bin`

For more information on how to configure this location,
see [*Pyright for Python*'s documentation][5].


## Why does this plugin have to perform saves so often?

Pyright does not support passing files from stdin.
[A feature request][6] was made and quickly rejected.

Adding an option that makes the plugin run only on "manual" saves
(the *Save All* action) is counter-productive, since that don't guarantee
the annotator class is called. This is [a known limitation][7].

If you use PyCharm Professional, you should be using
[the sister plugin][8] instead. It relies on experimental APIs and
thus not as stable, but much faster and does not require saving.


## Is watch mode (`--watch`) supported?

Supporting for watch mode is on the roadmap.
There is no ETA, however.


## Why does it take so long to run on my project?

There are multiple possible reasons for this.

### Other inspections are taking too long

Since this plugin invokes a CLI tool, it must be registered
as an [`ExternalAnnotator`][9]. Inspectors of this kind will
only run when all other background tasks have finished.

Check your other plugins to see if this is the case.

### There are a lot of files/things to process

Unlike Mypy, Pyright does not cache previous results in a hidden directory.
As such, everytime it runs on a given <em>file</em>, it also has to reprocess
all other files that file depends on.

Again, for better performance, [the sister plugin][8] is recommended.

### Your code triggers a Pyright bug

In some rare cases, Pyright might get itself into an infinite loop or similar.

If this seems to be the case, treat it as [a fatal error][10].


## Is this plugin affiliated with Microsoft/JetBrains?

No, or at least not in a business or ownership sense.

It was, however, created out of adoration of Pyright and JetBrains IDEs.


## I love this plugin. How can I support it?

You can consider [sponsoring it][8].

If you are feeling generous, see [`CONTRIBUTING.md`][11]
for how to contribute non-financially.


  [1]: https://microsoft.github.io/pyright/#/command-line?id=json-output
  [2]: https://github.com/microsoft/pyright/issues
  [3]: https://pypi.org/project/pyright/
  [4]: https://github.com/RobertCraigie/pyright-python/blob/HEAD/README.md#automatically-keeping-pyright-up-to-date
  [5]: https://github.com/RobertCraigie/pyright-python/blob/HEAD/README.md#modify-npm-package-location
  [6]: https://github.com/microsoft/pyright/issues/7282
  [7]: https://github.com/InSyncWithFoo/pyright-for-pycharm/issues/10
  [8]: https://github.com/sponsors/InSyncWithFoo
  [9]: https://plugins.jetbrains.com/docs/intellij/syntax-highlighting-and-error-highlighting.html#external-annotator
  [10]: problems.md#fatal-error
  [11]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/master/CONTRIBUTING.md
