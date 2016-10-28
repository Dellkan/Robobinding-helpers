# Robobinding-helpers
This project was created to make it easier to develop android applications with https://github.com/RoboBinding/RoboBinding.

Robobinding-helpers and Robobinding combined follows an approach similar to React, letting you set up anchors within your layout files that get connected to your datamodels.

It does the following:
 - Auto-generates boilerplate code required by robobinding (uses annotation preprocessing)
 - Add tons of utility on your model and presentationmodel, including connecting the two seamlessly.
 - Add validation on fields
 - Easily extract information from viewmodels into a `HashMap<String, Object>`, 
 suitable for upload to external API's (easily jsonified)
 - Combine/extend/include multiple models
 
 It works by looking at the annotations you put on your viewmodels, and from them, generates a brand new class filled with 
 all the things robobinding needs to function.
 
 Take a look at the wiki for some basics.

# Requirements
This library requires robobinding. It targets the robobinding version that does **not** use AspectJ

This library, and Robobinding both requires https://bitbucket.org/hvisser/android-apt.

# Install

    repositories {
  		...
  		maven {
  			url  "https://dl.bintray.com/dellkan/maven/"
  		}
  		...
    }
	
    dependencies {
      ....
      compile 'com.dellkan:robobinding-helpers-api:0.3.0'
      apt 'com.dellkan:robobinding-helpers-processor:0.3.0'
  	  ....
    }

Optionally, you may add the common library, which adds a bunch of common viewbindings, as well as a LayoutInflaterBuilder
    
    dependencies {
      ....
      compile 'com.dellkan:robobinding-helpers-common:0.3.0'
      ....
    }


# Issues
https://github.com/Dellkan/Robobinding-helpers/issues
