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
It automatically installs the actual NPM package and
places the original executables, named `pyright`/`pyright-langserver`,
inside the same virtual environment/directory as its own proxies
`pyright-python`/`pyright-python-langserver`.

These proxies will re-output the results of the original executables.
With the correct configurations, running the proxies will also
update the originals.

While the command-line interfaces are expected to be the same,
it might not be the case. Thus, you are recommended to use
the originals if you have them.


## Why does this plugin have to perform saves so often?

Pyright does not support passing files from stdin.
[A feature request][4] was made and quickly rejected.

Adding an option that makes the plugin run only on "manual" saves
is counter-productive. You have to save all your files manually,
or wait until PyCharm saves them automatically. Neither is convenient.

If you use PyCharm Professional, you should be using
[the sister plugin][5] instead. It relies on experimental APIs and
thus not as stable, but much faster and does not require saving.


## Is watch mode (`--watch`) supported?

Supporting for watch mode is on the roadmap.
There is no ETA, however.


## Why does it take so long to run on my project?

Since this plugin invokes a CLI tool, it must be registered
as an [`ExternalAnnotator`][6]. Inspectors of this kind are only run
when all other background tasks have finished.

Unlike Mypy, Pyright does not cache previous results in a hidden directory.
As such, everytime it runs on a given <em>file</em>, it also has to reprocess
all other files that file depends on.

Again, for better performance, [the sister plugin][5] is recommended.


## Is this plugin affiliated with Microsoft/JetBrains?

No, or at least not in a business or ownership sense.

It was, however, created out of adoration of Pyright and JetBrains IDEs.


## I love this plugin. How can I support it?

Please consider sponsoring [the sister plugin][5].

These plugins are similar in many ways and were created by the same author,
but the development for the other one requires (paid) subscriptions,
as the experimental LSP APIs are not yet available in PyCharm Community Edition.


  [1]: https://microsoft.github.io/pyright/#/command-line?id=json-output
  [2]: https://github.com/microsoft/pyright/issues
  [3]: https://pypi.org/project/pyright/
  [4]: https://github.com/microsoft/pyright/issues/7282
  [5]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm
  [6]: https://plugins.jetbrains.com/docs/intellij/syntax-highlighting-and-error-highlighting.html#external-annotator
