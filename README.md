# M-Shopping-ASOS-App
M-Shopping ASOS App



                                      ASOS Minimal Vialble Product

Use Case: Develop a minimal ASOS Fashion Browser app for iOS-Android. The user user must be able to see a list of product categories (for men and women), browse the products of each category, save items for later or see the full details. Please refer to the companion iOS_Exercise_Mockup.pdf for more details.

Notes
1.	Try to recreate the UI as shown in the mockup: white navigation bar, flat design, sidebar categories menu with tabbed interface to switch between men and women, ...
2.	Use caching and background processing to improve performances and responsiveness
3.	Avoid pre-build libraries if possible
4.	Use storyboards and Interface Builder
5.	Handle "missing connection" situations (ala-Facebook)
6.	Implement a custom "data model", where JSON objects are mapped to Obj-C objects
7.	Code organization / architecture and comments are important
8.	You can decide between Java, Obj-C and Swift
9.	If you can’t make everything, do just something but show quality and care in the code you write!
10.	Optional: make the app Universal and add a layout for the iPad (you decide how to render it). Or anything else that you think would fit well in this demo.
API Reference
Use the following Urls to get categories and products data from the web. The response data is in JSON format, and it should be self-descriptive.

Categories

for women:
https://dl.dropboxusercontent.com/u/1559445/ASOS/SampleApi/cats_women.json
for men:
https://dl.dropboxusercontent.com/u/1559445/ASOS/SampleApi/cats_men.json

ProductsByCategory

https://dl.dropboxusercontent.com/u/1559445/ASOS/SampleApi/anycat_products.json?catid={cat_id_here}
(output will be the same for all categories, anyway)

ProductDetails

https://dl.dropboxusercontent.com/u/1559445/ASOS/SampleApi/anyproduct_details.json?catid={prod_id_here}
(output will be the same for all products, anyway)






