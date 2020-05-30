# CuriousProductions.Root
Copyright (c) 2020 Gabriella Hotten (see license in LICENSE.md)
The core files of every CuriousProductions plugin.

**Setup**

Very little setup is required for this as it is only a dependency, so please read below for requirements and some instructions.

CuriousProductions plugins **require** a MySQL database and a Redis Server to function.

CuriousProductions plugins are built to run on Linux machines (and are not tested on Windows or Mac). CPS is designed to under a "cps" user account.
All plugins use CloudNet v3 (https://www.spigotmc.org/resources/cloudnet-v3-the-cloud-network-environment-technology.42059/), and therefore all CPS plugins must be used in a CloudNet environment.

To specify details such as MySQL/Redis login details, the network name, you must create (or copy the example of) a `network_data.yaml` file in the `/home/cps/` directory. Whilst servers will still startup without this file, it will use default values, which will most likely prevent modules such as Account Hub from starting. So please make sure you have this yaml file.

To enable the modules found in this dependency, you just have to just create an instance of this (ONLY DO THIS ONCE)
For example: `AccountHub accountHub = new AccountHub(this)`. This should be in the onEnable method of the plugin.
Please note that some modules will require extra paramaters to enable.

**List of Available Modules** (and the order they should be enabled it, yes this matters)
- Network Data Hub*
- Command Hub*
- Redis Hub*
- Account Hub*
- Proxy Manager*
- Punish Manager*
- Chat Hub*
- Staff Hub*
- Scoreboard Centre*
- Server Manager*
- Disguise Manager (ALPHA, should not be enabled (sorry this endedup in this branch))

* This is a **core** module and **must be enabled**.
