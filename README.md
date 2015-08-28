# Robobinding-helpers
This project was created to make it easier to develop android applications with https://github.com/RoboBinding/RoboBinding.
It does the following:
 - Auto-generates boilerplate code required by robobinding
 - Add validation on fields
 - Easily extract information from viewmodels into a `HashMap<String, Object>`, 
 suitable for upload to external API's (easily jsonified)
 
 Due to the focus on validation and gathering of data, this library is most useful in form-heavy applications.
 
 It works by looking at the annotations you put on your viewmodels, and from them, generates a brand new class filled with 
 all the things robobinding needs to function.
 
 I'll put in some documentation and get-started-introductions here soon.

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

# Issues
https://github.com/Dellkan/Robobinding-helpers/issues
