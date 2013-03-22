# hand-pallet

A pallet tutorial using virtualbox as a provider.

## Usage

This is a work in progress tutorial, thus far it just brings up a
single Debian node in virtualbox, with an admin user so we can ssh
into it.

A lot of this is cribbed from the following places, but I'm trying to
make things work with leiningen 2.1.1, virtualbox 4.2, the pre-release
pallet 0.8 snapshots, and document the process and problems in one
location.

 * https://github.com/pallet/pallet-vmfest
 * https://github.com/tbatchelli/vmfest
 * https://gist.github.com/tbatchelli/867526/raw/2e1238b8ddc7c888403614f0b906cf077380bf15/pallet-vmfest.md

I hope to proceed this tutorial beyond what is listed there, but this
is what I have documented thus far. *WARNING* I am likely cargo
culting in a number of places and am hoping others can explain what
the correct approach is.

Before we proceed go install
[leiningen](https://github.com/technomancy/leiningen), and install
[virtualbox](https://www.virtualbox.org/wiki/Downloads). I ran this on
an Ubuntu box, so was unable to use the XPCOM interface, thus the
dependency on vboxjws.

I installed VirtualBox previously for Vagrant, so I removed
`~/VirtualBox VMs`, ran `VirtualBox` and removed existing VMs. I had
also previously made use of vmfest, and much of the metadata is stored
in `.vmfest`, so I removed that as well. I don't think this was
necessary but sometimes it's helpful to start with a clean slate.

In a separate shell run

    $ VBoxManage setproperty websrvauthlibrary null # first time only
    $ vboxwebsrv -t0

Open a repl in the project using `lein repl`, or your nrepl client of
choice and follow along in the [code](src/hand_pallet/core.clj).

## Errors

I encountered a number of errors along the way, some were clearly my
fault but added to my confusion. I will submit bug reports once I'm
certain which projects they are associated with, or that they are
actual bugs. I'm documenting them here in case someone else encounters
them.

### Lein Pallet Help

    $ lein pallet help

Midway though the output I encounter this error:

> version          - Print Pallet's version to standard out.
> vmfest-script failed to load: java.lang.RuntimeException: No such var: compute/compute-service, compiling:(pallet/task/vmfest_script.clj:18)
>
> Run pallet help $TASK for details.

### Lein pallet up

    $ lein pallet up

Just seems to hang and then complains about adjusting locked nodes.
I'm guessing I need to provide a hook for a specific groupspec to
converge, or converge first but I'm confused by this.

## License

Copyright © 2013 Charles L.G. Comstock

Distributed under the Eclipse Public License, the same as Clojure.
