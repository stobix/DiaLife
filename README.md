# DiaLife

A tiny demo project to showcase Clean Architecture, MVVM, Room interaction and dependency injection (via Koin).

This simple demo app lets the user add "diary entries" consisting of an icon, a title, and some text, and to remove the entries again.

The backend can be Room db storage, or in-memory storage with or without predefined elements, changed by switching di sources.

There are two icon sets to choose from. This too is done by dependency injection.

To change active data backend or icon set, change which line is commented out in MainActivity.onCreate, lines 114-120.

