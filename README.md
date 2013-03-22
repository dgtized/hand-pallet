# hand-pallet

A pallet tutorial using virtualbox as a provider.

## Usage

This is a work in progress tutorial, thus far it just brings up a
single Debian node in virtualbox, with an admin user so we can ssh
into it.

A lot of this is cribbed from the following places, but I'm trying to
make things work with leiningen 2.0, virtualbox 4.2, the pre-release
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
an Ubuntu box, so was unable to use the XPCOM interface.

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

I know the addition of a `:pallet` profile to leiningen is documented
in a few places, but I couldn't find a good overview. For instance I
put the dependency on `vboxjws` in my project dependency block but
[pallet-lein](https://github.com/pallet/pallet-lein#default-pallet-dependencies)
documents putting it in the dependencies for `:pallet` profile. My
guess is this is much of the cause of my `lein pallet` difficulties.

### Lein Pallet Help

    $ lein pallet help

Midway though the output I encounter this error:

> version          - Print Pallet's version to standard out.
> vmfest-script failed to load: java.lang.RuntimeException: No such var: compute/compute-service, compiling:(pallet/task/vmfest_script.clj:18)
>
> Run pallet help $TASK for details.

### Lein Pallet commands without a project.clj

Executing some lein pallet sub-commands outside of the project results
in the following error:

```
$ cd ~
$ lein pallet up
#
# A fatal error has been detected by the Java Runtime Environment:
#
#  SIGSEGV (0xb) at pc=0x00007fedbc809dcb, pid=25232, tid=140659038054144
#
# JRE version: 6.0_26-b03
# Java VM: Java HotSpot(TM) 64-Bit Server VM (20.1-b02 mixed mode linux-amd64 compressed oops)
# Problematic frame:
# V  [libjvm.so+0x516dcb]  unsigned+0xfb
#
# An error report file with more information is saved as:
# /home/clgc/code/hs_err_pid25232.log
#
# If you would like to submit a bug report, please visit:
#   http://java.sun.com/webapps/bugreport/crash.jsp
#
Subprocess failed
```

This seems to apply to the pallet images, nodes, and converge options.
While clearly the command can't execute without the project.clj config,
it seems like a helpful error message instead of a segfault would be
useful.

### Lein pallet add-service

Supposedly after running lein pallet project-init you can add a vbox
service, but I encountered the following error:

```
$ lein pallet add-service vbox vmfest
Wrong number of args (4) passed to: add-service$add-service
Add a service provider definition to your pallet configuration.
This will create ~/.pallet/services/service-name.clj
  lein pallet add-service service-name provider-name? identity? credential?
```

### Lein pallet up

    $ lein pallet up

Just seems to hang and then complains about adjusting locked nodes.
I'm guessing I need to provide a hook for a specific groupspec to
converge, or converge first but I'm confused by this.

## License

Copyright © 2013 Charles L.G. Comstock

Distributed under the Eclipse Public License, the same as Clojure.
