# Pyright for PyCharm user documentation

This site serves as the user documentation for
the <i>Pyright for PyCharm</i> project.
The project includes two plugins:

|                  | The CLI-based plugin     | The LSP-based plugin                |
|:-----------------|:-------------------------|:------------------------------------|
| Repository       | [pyright-for-pycharm][1] | [pyright-langserver-for-pycharm][2] |
| Marketplace name | <i>[Pyright][3]</i>      | <i>[Pyright Language Server][4]</i> |
| Codename         | <i>CLI</I>               | <i>LSP</i>                          |

In this documentation, the two plugins
will be referred to using their codenames.


## Choosing the right plugin

!!! abstract "TLDR: Professional --> LSP; Community --> CLI."

These two plugins have the same naming conventions,
are maintained by the same person, have similar (but not identical)
configurations, but are meant for different target users.

LSP is dependent on [the experimental language server protocol APIs][5]
which are only available for PyCharm Professional and other paid IDEs
since 2023.2. This means that PyCharm Community users cannot use it.

On the other hand, CLI can be used by both.
However, due to its limited capabilities and performance reason,
it is recommended that PyCharm Professional users use LSP instead.

!!! warning

    Do not install both plugins.
    While doing so will likely cause no technical issues,
    their functionality overlaps a lot.


  [1]: https://github.com/InSyncWithFoo/pyright-for-pycharm
  [2]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm
  [3]: https://plugins.jetbrains.com/plugin/24145
  [4]: https://plugins.jetbrains.com/plugin/24146
  [5]: https://plugins.jetbrains.com/docs/intellij/language-server-protocol.html
