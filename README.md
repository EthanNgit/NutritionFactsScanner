# NutritionFactsScanner
![Screen_Recording_20231001_124244_NutritionLabelTest](https://github.com/EthanNgit/NutritionFactsScanner/assets/105979510/a0e7c70d-64d0-4c51-9066-c6e055c6344b)
## About
This was made to put in my [Nutrition application]([https://pages.github.com/](https://github.com/EthanNgit/NutritionProject)https://github.com/EthanNgit/NutritionProject) as an increase in UX. </br>
I thought it would be interesting for others to be able to see and use if needed the specific chunck used to make it happen. </br>
## How it works
The algorithm is fairly simple so I will give a quick summary here.
- First use Google ML kit (OCR) to parse words
- add lines to the queue of the parser
- The parser will use the lines provided to try match to a pattern
- If lines dont match a pattern, try a unique approach
- Otherwise give up and move to the next line
This is really oversimplified, where I did not talk about the extensive use of set searching, but I think based on that description you can get the general idea.</br>
## Known issues
- [ ] Major drawback is the bottlecap of search time due to the OCR accuracy
- [ ] Calories is hard to capture (when the ocr does not pick it up as a single line)
- [ ] No Serving size support (would have to devise a new method to parsing atleast the grams)
</br>
## Other
> Last updated 10/1/2023
