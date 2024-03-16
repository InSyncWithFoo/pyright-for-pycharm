## Frequently asked questions


### What's the difference between `pyright` and `pyright-python`?

Pyright is written in TypeScript and published on NPM,
mainly for the purpose of better integration with VSCode.
This requires a dependency on Node, which Python developers
might not have on their development machines.

[The `pyright` package on PyPI][1] was created to solve this problem.
It automatically installs the actual NPM package and
places the original executables, named `pyright`/`pyright-langserver`,
inside the same virtual environment/directory as its own proxies
`pyright-python`/`pyright-python-langserver`.

These proxies will simply output the results of the original executables.
With the correct configurations, the originals will be updated automatically.

While the command-line interfaces are expected to be the same,
it might not be the case. Thus, you are recommended to use
the original if you have them.


  [1]: https://pypi.org/project/pyright/
