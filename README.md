# hand-pallet

A simple pallet tutorial using virtualbox as a provider.

## Preface

In this tutorial, pallet will define a simple Debian VM using
Virtualbox as a "cloud provider" and install an admin user on the VM
with a matching username.

This uses leiningen 2.1.1, virtualbox 4.2, and the latest published
pallet jar files. At the time of this writing, pallet 0.8 is in
snapshot prior to release. A lot of this is cribbed from the following
places, but I wanted to document the process and problems in one
location.

 * https://github.com/pallet/pallet-vmfest
 * https://github.com/tbatchelli/vmfest
 * https://gist.github.com/tbatchelli/867526/raw/2e1238b8ddc7c888403614f0b906cf077380bf15/pallet-vmfest.md
 * https://github.com/pepijndevos/irc-deploy

## Usage

Install [leiningen](https://github.com/technomancy/leiningen), and
[virtualbox](https://www.virtualbox.org/wiki/Downloads).

In a separate shell run

    $ VBoxManage setproperty websrvauthlibrary null # first time only
    $ vboxwebsrv -t0

Take a look at the configuration in [project.clj](project.clj), and
[pallet.clj](pallet.clj) in the project root, as they specify which
dependencies, default provider, image and group to install. This was
run on an Ubuntu box, so the VirtualBox XPCOM interface is broken,
hence the dependency on vboxjws.

Specify a `~/.pallet/config.clj` using `lein pallet config` to
generate but then copy the one provided in the project from
[pallet-config.clj](pallet-config.clj).

    $ lein pallet config
    $ cp pallet-config.clj ~/.pallet/config.clj

Adjustments to the `:default-network-type`,
`:default-local-interface`, and `:default-bridged-interface` per the
documentation at https://github.com/pallet/pallet-vmfest#configuration
may be required.

Download a debian or ubuntu image:

    $ lein pallet add-vmfest-image \
        https://s3.amazonaws.com/vmfest-images/debian-6.0.2.1-64bit-v0.3.vdi.gz

or

    $ lein pallet add-vmfest-image \
        https://s3.amazonaws.com/vmfest-images/ubuntu-12.04.vdi.gz

Boot up the vm:

```
$ lein pallet up
19:20:58.238 [operate-13] WARN  clj-ssh.ssh - Permanently added '192.168.56.102' (RSA) to the list of known hosts.
|    :primary-ip | :private-ip |     :hostname | :group-name | :roles |
|----------------+-------------+---------------+-------------+--------|
| 192.168.56.102 |             | hand-pallet-0 | hand-pallet |        |
```

After finding the public IP from the above command, ssh into the node.
The `debian-spec` group specification in
[core](src/hand_pallet/core.clj) installs an admin user with matching
username with `automated-admin-user`.

    $ ssh 192.168.56.102

The default vmfest user baked into the VM also works

    $ ssh vmfest@192.168.56.102 # using password vmfest

To query the running nodes

```
$ lein pallet nodes
|    :primary-ip | :private-ip |     :hostname | :group-name | :roles |
|----------------+-------------+---------------+-------------+--------|
| 192.168.56.102 |             | hand-pallet-0 | hand-pallet |        |
```

*NOTE:* This query overlaps with Vagrant and anything else using
 VirtualBox, so it may show nodes not managed by Pallet.

Once complete, destroy the VM with:

    $ lein pallet down

## REPL

Run this tutorial in a repl by following along in
[repl.clj](src/hand_pallet/repl.clj) and referencing
[core.clj](src/hand_pallet/core.clj).

## Switching between Ubuntu and Debian Image

By using selectors, it is possible to select which image to use:

    $ lein pallet up -s ubuntu
    $ lein pallet down

This is kind of a hack, it's not possible to bring up one of each
using this method. If a node is already created it appears to ignore
the selector. This is why down works without specifying a selector.

## Initializing Another Pallet Project

To initialize another project like this one, execute the following
commands. The comments explain which files are provided by each
command.

    $ lein new hand-pallet      # project.clj
    $ cd hand-pallet
    $ lein pallet project-init  # pallet.clj

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
>
> vmfest-script failed to load: java.lang.RuntimeException: No such var: compute/compute-service, compiling:(pallet/task/vmfest_script.clj:18)
>
> Run pallet help $TASK for details.

### Lein pallet up hangs

When I first wrote this tutorial I had problems with `lein pallet up`
hanging.

I installed VirtualBox previously for Vagrant, so I removed
`~/VirtualBox VMs`, ran `VirtualBox` and removed existing VMs. I had
also previously made use of vmfest, and much of the metadata is stored
in `.vmfest`, so I removed that as well. I don't think this was
necessary but sometimes it's helpful to start with a clean slate.

**IMPORTANT** However, I did have stale networking configuration in
`~/.VirtualBox/VirtualBox.xml`. So I ran `rm -rf ~/.VirtualBox` to fix
this, and then restarted `vboxwebsrv -t0` after clearing out existing
nodes. Prior to that I could not execute `lein pallet up` as it hung
waiting for an IP. This cascaded into a locking exception when it
failed to remove the node it could not connect to.

### lein pallet add-vmfest-image fails when /tmp is full

It's possible to fix this area by either expanding `/tmp`, specifying
the temp directory java uses with the java property `java.io.tmpdir`,
or by unpacking the image on another drive an then installing the
unpacked version as follows:

```
$ wget https://s3.amazonaws.com/vmfest-images/ubuntu-12.04.{vdi.gz,meta}
$ gunzip ubuntu.12.04.vdi.gz
$ lein pallet add-vmfest-image ./ubuntu-12.04.vdi
```

## License

Copyright © 2013 Charles L.G. Comstock

Distributed under the Eclipse Public License, the same as Clojure.
