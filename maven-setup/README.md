Minigame Maven Setup
====================

Install spigot
--------------

To be able to compile minigames using maven you need access to the spigot binaries.
After building Spigot with build tools you can use maven to deploy the artifacts to your own maven repository.

To install spigot in your local repository invoke

```

   mvn install
   
```

To deploy spigot to your web repository invoke

```

   mvn deploy -DaltDeploymentRepository=<your-repos-id>::default::http://<your-repos>
   
```

The minigames lib itself currently only depends on the newest version of spigot.



Install nms builds
------------------

As a second goal we require support for nms classes. NMS classes are special variants that highly depend on the minecraft or craftbukkit api. The differ from version to version.

For a clean support each minigame and the lib decide which version the current server is build on.

For maven builds to not mess up with this dependencies we decided to use a special shadowed artifact only containing the version specific classes.

You will find a sub directory called nms-server-poms containing the pom files you need. 

To install the nms builds in your local repository invoke

```

   mvn install
   
```

To deploy the nms builds to your web repository invoke

```

   mvn deploy -DaltDeploymentRepository=<your-repos-id>::default::http://<your-repos>
   
```

