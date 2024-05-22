## UI hints

As a path field is edited, the small hint under the field
will show whether the path is valid or invalid.

This is only used to give a general hint;
a path can still be saved even if it is marked as invalid.


## Inspections

The inspections shown in the [<i>Highlight severity levels</i>][1] section
can be disabled, though this is not recommended.
The effect of doing so is different for each plugin.

* For CLI:
    * The executable will not be executed.
    * The files will not be automatically saved.

* For LSP:
    * New server(s) will not be started.
    * Existing server(s) will simply stop on [restart][2].


  [1]: ../configurations/common.md#highlight-severity-levels
  [2]: ../how-to.md#how-to-restart-the-language-server
