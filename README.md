# Dithering-GUI-implementation
A fully featured GUI for image dithering using processing

Made with processing 3.0 in mind, may also work in Processing 2.0+.

Just open the .pde in processing, press run code and the GUI will load,
don't forget to select one or multiple files before pressing start!

## Features

-Ordered dithering, Floyd Steinberg Dithering, Random Dithering and Atkinson Dithering

-random color selector, takes random colors from the image!

-looper, for when using random colors, dithers each image a multitude of times with different random colors

-multi image processing (just select multiple images in the image selector)

-color picker! choose which colors to dither with!

-bayermatrix selector for ordered dithering!

-color scales! scales your colors to create better results!

##New Features

+made application standalone, either by making the ~~code export with processing~~ or recreating in normal Java

[temp link] (http://sdziscool.polyphemus.feralhosting.com/Downloads/)

with this the project will slowly move to a processing independant implementation for better performance.

+main.java added for a start, now only requires you to import processing.
      
+the pde now also exports correctly so you can compile for yourself!

+pde/jar is completely independant

##TODO

-add more dithering algorithms

-give more info and feedback to the user(~~file selected or not?~~, image preview)

-multithreading?

-clean up code... maybe

## Crashes and bugs
The program is rather crashsafe, it can throw some exceptions before starting(while selecting etc),
but once you press start and have an image selected it's smooth sailing



-the pde now also exports correctly so you can compile for yourself!
