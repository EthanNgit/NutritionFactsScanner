# NutritionFactsScanner
Experiment to see if I can design a Nutritional Label scanner to take data from it quickly. This is meant as a UX feature for my Nutritional project. <br/>

## The Idea
![image](https://github.com/EthanNgit/NutritionFactsScanner/assets/105979510/4b6c3027-714a-470d-8dfd-2c997adb4f65) <br/>

Looking at the FDA guideline label you can notice a few patterns that might help create an algorithm to aid me. <br/>
  
![image](https://github.com/EthanNgit/NutritionFactsScanner/assets/105979510/acb82ce6-eb90-4ff0-9166-087b231ff640) <br/>

Here I describe it (although the writing is hard to read), it describes the following:
* Nutritional values are all on a single line (in red)
* And All things we can ignore for sure are next to a '%', as you can calculate daily value more accurately to a person manually (in pink)

<br/> So the ML kit from google should be able to pick these up, somewhat easily. The problem is, it does not. So to help it, we use a video, not just a picture, so that the user can scan around a label and it can try to parse data while they do it. Given that this is bound to be extremely simple of an algorithm (I will describe the algorithm further down), it might never pick any information, but it is still better UX wise then nothing (which is the goal). <br/>

## Latest Update
The algorithm was put through some improv, but the general idea works well. I got it to scan calories as of now, but the rest should be kinda rinse and repeat. (will upload the project files soon)
Here is a quick picture of what it looks like now

![phone](https://github.com/EthanNgit/NutritionFactsScanner/assets/105979510/9f86e036-134c-44d3-a967-2170ed52ae29)
