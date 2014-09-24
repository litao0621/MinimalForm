MinimalForm
===========

Simplistic, single input view form

# ScreenShot

![Image][1]

# Usage

``` java
  MinimalFormLayout mForm=(MinimalFormLayout)findViewById(R.id.form);
  mForm.build(List<String> titlelist);
  //mForm.build(List<String> titlelist,List<Integer> inputtype);
  //mForm.build(List<String> titlelist,List<Integer> inputtype,List<String> verify,List<String> errormsg);
  mForm.setOnSubmitListener(SubmitListener listener);
  mForm.setSuccessMsg("Thank you! We'll be in touch.");

```



###[See the layout code][2]

* Add some view to minimalformlayout(TextView[1,2,3],EditText,ImageButton,ProgressBar) 
* Add the drawable for the progress bar [like this][3]
* Add tag to a different TextView (title,validator,page)

and copy this into your `libs` directory.
-   [`NineOldAndroid-2.4.0`](https://github.com/downloads/JakeWharton/NineOldAndroids/nineoldandroids-2.4.0.jar)

-   [`MinimalForm-1.0`](https://github.com/sd6352051/MinimalForm/blob/master/releases/minimalform-1.0.jar?raw=true)

# License
Copyright 2014 litao.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.







[1]: https://raw.githubusercontent.com/sd6352051/MinimalForm/master/screenshot/form.gif
[2]: https://github.com/sd6352051/MinimalForm/blob/master/app/src/main/res/layout/activity_main.xml#L8-L67
[3]: https://github.com/sd6352051/MinimalForm/blob/master/app/src/main/res/drawable/progressbar_shape.xml
