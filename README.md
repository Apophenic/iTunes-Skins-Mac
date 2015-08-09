# iTunes Skin Tools for Mac
--------------
![Sample Img](https://github.com/Apophenic/iTunes-Skin-Tools/blob/master/iTunesSkinTools/res/sample/sample.jpg)
(Windows sample)
iTunes Skin Tools makes creating skins / themes for iTunes in Mac far more seamless and accessible by
automating
the process of injecting and extracting resource files into iTunes.rsrc.

### How It Came To Be
---------------------
Have your eyes grown weary and fatigued from this incessant trend of acute black text on exceedingly high contrast white
backgrounds that perpetuates in all facets of contemporary software design resulting in sterile user interfaces? Was
 that an overly drawn out sentence?
If you answered yes to both questions, read on.

### About iTunes.rsrc
----------------
```iTunes.app/Contents/Resource/iTunes.rsrc``` is a Mac Resource File that contains embedded resources for iTunes,
primarily .png images used for the UI. Each resource has a unique Integer ID.

### iTunes.rsrc IDs
-------------------------
[Click here](http://htmlpreview.github.io/?https://github.com/Apophenic/iTunes-Skin-Tools/blob/master/Resource%20IDs/ResourceIDs.html) for a [comprehensive] guide of how resource IDs effects the UI.

This guide was created using the Windows version of iTunes, but resource IDs are interchangeable between platforms.
Note: For older versions of iTunes, most resource IDs remain unchanged, however some don't exist.

### What iTunes Skin Tools Does
---------------------------
iTunes Skin Tools currently supports extracting all image resource files from iTunes.rsrc as well as injecting resources
back into the .rsrc file. Extracted files are named after their corresponding resource ID (i.e. _5000_ will be
extracted as _5000.png_). In a typical use case, you'll extract all resources, edit IDs of your choosing, then inject
the modified files back into iTunes.rsrc.

### How To Use It
-----------------
~~~~~~~~ bash
java -jar iTunesSkins-Mac.jar -op=extract|inject -itunesdir="/Applications/iTunes.app"
-workingdir="/Users/Justin/workingdir" -createbackup=true
~~~~~~~~
* _op_: __Extract__ will extract all files from iTunes.dll into _workingdir_, while __Inject__ will inject all
files from _workingdir_ into iTunes.dll.
* _itunesdir_: iTunes.exe's parent directory
* _createbackup_: Optional, will create a backup of iTunes.dll if one doesn't already exist. Default behavior is ```true```.

### Compatibility
-----------------
* Support for latest iTunes (12.2.0.145)
* iTunes Versions 10.6 and greater
* x32 and x64 versions of iTunes