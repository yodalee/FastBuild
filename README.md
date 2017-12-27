FastBuild
=========

FastBuild is a Minecraft plugin that let you break/build your building faster.
It lets you:
* Break blocks in one direction at once
* Build blocks in one direction at once

There are two youtube video that demo how this plugin do:
[build function](https://www.youtube.com/watch?v=pc9FjvXC7kY)
[break function](https://www.youtube.com/watch?v=qWxviuqtntw)

FastBuild is open source and is available under the GNU General Public License v3.

# Where to download:

I add a compiled fastbuild.jar against to spigot-1.12.2.jar in plugin directory.

# How to build plugin:

1. Download Spigot server BuildTool.jar at [Download Spigot](https://hub.spigotmc.org/jenkins/job/BuildTools/)
2. Built the Spigot server by `java -jar BuildTool.jar --rev <version>`. I use version 1.10.2 here.
3. Put output `spigot-<version>.jar` in lib directory as `spigot.jar`.
4. Install ant: using `sudo apt-get install ant` in Ubuntu, or other package managers of your release.
5. Simply enter `ant` will build the plugin `fastbuild.jar`.

# How to run it:
1. Run a server spigot or craftbukkit server.
2. Put `fastbuild.jar` into plugins directory in your server.
3. Reload or Re-open server.

