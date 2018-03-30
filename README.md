![Travis build Status](https://travis-ci.org/rvoortman/png-decoder.svg?branch=master)

# PNG Decoder Written in Kotlin
A very simple PNG decoder written in Kotlin. This started as a small stand alone project to learn about the PNG format and a chance for me to better understand Kotlin. I'm planning to make this a fully functional decoder that can be used in any project you like.

Although the library is functional, it is far from fully featured. Some basic PNG functionalty is missing, like interlacing and transparency. 

## Usage
Clone this repository. To read a PNG image use the following:
```kotlin
val image = PNGReader.readPng(args.first())
```
This will return a PNGImage, as can be found in the models folder. This contains all meta information, including width, height, colortype and more. 

To generate a BufferedImage from this, just call:
```kotlin
val bufferedImage = ImageData.toBufferedImage(image)
```

You can render this `BufferedImage` as you like.
## Todo
- [x] Transforming the imagedata into an actual image
- [ ] Write tests using the Spek framework
- [x] Comment the code
- [x] Add a linter and travis to the repository.
