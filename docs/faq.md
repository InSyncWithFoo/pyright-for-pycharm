# Frequently asked questions


## What exactly is this plugin doing?


### CLI

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


### LSP

Most of the logic is already defined by either the [@eclipse-lsp4j/lsp4j][2]
library or [the experimental language server protocol APIs][3].
LSP simply glues these and the Pyright language server together.

Again, the executable is not checked.
You can use your own hand-hacked version if you feel like it.


## My code should have no/these errors, but it does/doesn't.

Both CLI and LSP contain no type checking logic.
For type checking bugs, please report them to [the Pyright issue tracker][4].


## What's the difference between the `pyright` and `pyright-python` files?

Pyright is written in TypeScript and officially published on NPM,
mainly for the purpose of better integration with VSCode.
This requires a dependency on Node, which Python developers
might not have on their development machines.

[The PyPI `pyright` package][5] was created to solve this problem.
When installed, it places 4 proxy executables in
the same virtual environment/directory you have your `pip` in:

* `pyright`/`pyright-langserver` (these have the same names as the originals)
* `pyright-python`/`pyright-python-langserver`

These proxies will automatically install the actual NPM package
if it is not already installed, then re-output the results of
the original executables. [With the correct configurations][6],
new versions may be automatically installed at runtime.

If the corresponding version of the NPM package has not been installed,
the proxies will also re-output the "added 1 package" notice by NPM,
which <em>will</em> cause parsing errors.
Due to this, it is recommended that the original executables are used instead.

Said original executables can typically be found at:

* Windows: `%HOMEPATH%/.cache/pyright-python/<version>/node_modules/.bin`
* Linux: `~/.cache/pyright-python/<version>/node_modules/.bin`


## Why does CLI have to perform saves so often?

Pyright only reads actual files on disk.
It does not support passing files from stdin.
[A feature request][7] was made and quickly rejected.

Adding an option that makes CLI run only on "manual" saves
(the *Save All* action) is counter-productive, since that doesn't
guarantee the annotator class is called. This is [a known limitation][8].

If you use PyCharm Professional, you [should be using][9] LSP instead.


## Is the command-line watch mode (`--watch`) supported?

Support for `--watch` is on the roadmap.
There is no ETA, however.


## Why does CLI take so long to run on my project?

There are multiple possible reasons for this.

### Other inspections are taking too long

Since CLI invokes a command-line tool, it must be registered
as an [`ExternalAnnotator`][10]. Inspectors of this kind will
only run when all other background tasks have finished.

Check your other plugins to see if this is the case.

### There are a lot of files/things to process

Unlike Mypy, Pyright does not cache previous results.
As such, everytime it runs on a given <em>file</em>,
it also has to reprocess all other files that file depends on.

Again, for better performance, LSP [is recommended][9].

### Your code triggers a Pyright bug

In some rare cases, Pyright might be stuck in an infinite loop or similar.

If this seems to be the case, treat it as [a fatal error][11].


## Is this plugin affiliated with Microsoft/JetBrains?

No, or at least not in a business or ownership sense.

It was, however, created out of adoration of Pyright and JetBrains IDEs.


## I love this project. How can I support it?

You can consider [sponsoring it][12].

If you are feeling generous, see [`CONTRIBUTING.md`][13]
for how to contribute non-financially.


  [1]: https://microsoft.github.io/pyright/#/command-line?id=json-output
  [2]: https://github.com/eclipse-lsp4j/lsp4j
  [3]: https://plugins.jetbrains.com/docs/intellij/language-server-protocol.html
  [4]: https://github.com/microsoft/pyright/issues
  [5]: https://pypi.org/project/pyright/
  [6]: https://github.com/RobertCraigie/pyright-python/blob/HEAD/README.md#automatically-keeping-pyright-up-to-date
  [7]: https://github.com/microsoft/pyright/issues/7282
  [8]: https://github.com/InSyncWithFoo/pyright-for-pycharm/issues/10
  [9]: index.md#choosing-the-right-plugin
  [10]: https://plugins.jetbrains.com/docs/intellij/syntax-highlighting-and-error-highlighting.html#external-annotator
  [11]: problems.md#fatal-error
  [12]: https://github.com/sponsors/InSyncWithFoo
  [13]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/master/CONTRIBUTING.md
