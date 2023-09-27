# NutritionFactsScanner
Experiment to see if I can design a Nutritional Label scanner to take data from it quickly. This is meant as a UX feature for my Nutritional project.

The Idea
![image](https://github.com/EthanNgit/NutritionFactsScanner/assets/105979510/4b6c3027-714a-470d-8dfd-2c997adb4f65)

- Looking at the FDA guideline label you can notice a few patterns that might help create an algorithm to aid me.
  
![image](https://github.com/EthanNgit/NutritionFactsScanner/assets/105979510/acb82ce6-eb90-4ff0-9166-087b231ff640)

- Here I describe it (although the writing is hard to read), it describes the following:
* Nutritional values are all on a single line (in red)
* And All things we can ignore for sure are next to a '%', as you can calculate daily value more accurately to a person manually (in pink)

- So the ML kit from google should be able to pick these up, somewhat easily.
- The problem is, it does not.
- So to help it, we use a video, not just a picture, so that the user can scan around a label and it can try to parse data while they do it.
- Given that this is bound to be extremely simple of an algorithm (I will describe the algorithm further down), it might never pick any information, but it is still better UX wise then nothing (which is the goal).

- The Algorithm will parse the given callback string from ML kit and check for key words like Calories, now since we know that calories should be on the same line as the calorie amount, we can associate that value to calories
- This will loop for every value possible, until it finds them all on the label, or until the user decides to stop the scanner.
- Note that there will be some bridges that will be crossed for example if it reads Sat. Fat, and its supposed to be Saturated Fat, it will fail, so we have to create a base case for everything that might be a problem.
