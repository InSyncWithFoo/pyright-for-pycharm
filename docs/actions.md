The plugins provide some IDE actions that
are accessible via the <i>Actions</i> panel.


## Copy Pyright command

This action copies the Pyright command
that would be used for the current file to clipboard.

It does not differentiate file types and
thus will be available for every file
even if the annotator doesn't run on the given file.

!!! note

    The copied command is not OS-dependent and
    may not work as-is when pasted into the shell.
