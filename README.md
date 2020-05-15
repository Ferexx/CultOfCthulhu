# CultOfCthulhu
COMP30880 project, Team Mojack
Jack Price
Monika Pociute
Rohan Shivakumar

### Sprint 1:

We used Spring because creating a UI with HTML is far easier than doing it with something like Swing or AWT.

Staff/Project generation:
The program generates staff members from any sample .csv file. In our test cases we used the sample file provided on Brightspace. Staff projects are generated by randomly choosing a string of words from a static list stored in the program. It generates three projects per staff member, and outputs to a .txt file.

Student generation:
Student generation is done by taking a first name and a last name from two static files, and assigning a student id that starts at 0 and increases by 1 every time a new student is created. Stream is decided by randomly generating an integer between 0 and 10. If the number is less than 6, the student is assigned to CS. If the number is greater than or equal to 6, the student is assigned to DS.

Project preference generation:
Project preferences are generated on a per-student basis. What this means is that each time we create a student, we make a call to the project assignment function. This function uses the nextGaussian method of Random, with the values adjusted to suit our needs.
We found this page explaining how to do this: https://www.javamex.com/tutorials/random_numbers/gaussian_distribution_2.shtml

### Sprint 2:

We added new classes for staff members and Projects. This required some significant restructuring of the parsing code to load the information into their relevant classes instead of storing it as lists of strings.

The work was tested using the csv uploaded in Sprint 1. A catch-all error page was added that redirected users to the beginning of the process in case something went wrong.

### Sprint 3:

While the function used to generate a solution in our SolutionByLottery class returns a map, that map is only used as a Thymeleaf attribute for the html page. Aside from that, it is discarded right away. The mapping of students to projects is done within each instance of the student and project class. Student has a field contains the ID of the project to which they were assigned. Similarly, Project has a field which contains the ID of the student it was assigned to.

### Sprint 4:

We added a new Solution class so that our Simulated Annealing and Genetic Algorithms classes will be able to compare two different solutions.
We also began work on a testing suite, run "ProjectAllocationApplicationTests" to run them all in one go.

We seriously upgraded our UI to make it look a lot more presentable. We also began work on functionality that would allow students to denote their preferences within the app itself. While the frontend for this is mostly done, there is no backend implementation yet.
In the future, we hope to use the UCD login system to allow students to login and denote their preferences, though we are still waiting to hear back from UCD on that one.

The new Simulated Annealing and Genetic Algorithms classes are not fully in use yet. SolutionController does create instances of them, but doesn't actually do anything with them just yet. Next week when we are changing solutions and deciding if we should accept them, this will come more into play.

Our "change" functionality is stored in the new Solution class. It simply swaps the projects of two random students. It does this multiple times depending on an input variable.

### Sprint 5:

This week saw a fairly significant amount of changes. We updated our solution assessment method so that we're not directly accessing the student class to get their assigned projects. Instead we use the <Integer, Integer> map we were already generating to check which students have been assigned to which subjects. We also included the GPA check in our preference check loop to make the algorithm run faster.

We added the option for users to upload a file containing their own students, instead of having the application randomly generate students for them. This is very useful for testing, as you can check if you get expected output with certain inputs. For example, the sample file we provide, "EveryoneGets1st.csv", includes student preferences which should result in every student getting their first preference.

We also improved our change functionality. Now, instead, of randomly swapping the projects of two students, the application swaps the order in which we assign projects to students, in our initial "SolutionByLottery" generation.

We output the energies of new solutions, and whether they were accepted, to console. We also output the final solution to console as well as to the console.

If we'd had more time we would have updated the web interface to something more similar to what we're outputting in console, as well as an analysis of how well the algorithm worked.

### Sprint 6:

We created the class "GeneticAlgorithmSolutionHerd" to store the solutions, and provide basic functionality. The solutions are stored in the standard Java-defined list. Our class is effectively a wrapper, that provides methods specific to the genetic algorithm application, like culling solutions from the list, etc.

Our main function in GeneticAlgorithm is the unimaginatively named "runAlgorithm". It starts by generating random solutions until the max population size is reached, and then enters the main loop. The main loop has the basic cycle of mate the best solutions until max population size is reached, sort solutions according to their fitness, then cull the worst solutions.

Our mate function works by choosing two random solutions from the top x% of the solution herd, where x defaults to 30, but can be changed by the user. It interleaves the project assignment order of these two solutions. It does this by iterating through the students in order.
For each solution, it finds the index of the current student, and tries to place the student at the same index in the new solution. If it can't do this, it tries to place the student at the next index, and repeats until it finds an empty index. If it were to reach the end of the array, it finds the last available position in the array, and then increment the index of everything after. This method will never fail due to the pigeonhole principle.

Our mutation function reuses the change functionality introduced for simulated annealing. It simply swaps the positions of two adjacent students in the assignment order.

We assess solutions in a similar way to how we assessed solutions for simulated annealing, except we now increase fitness when something is good, and decrease it when something is bad.

We updated our web interface to display the solution output we see in console. It now displays each student, their GPA, the preference they got, and the title of the project they got.

### Sprint 7:

This week was focused on UI, which was something we had already done in previous weeks. To compensate for this, we decided to rework our existing UI to make it more user-centric.

To do this, we had family members try to use the program, and see what were their biggest issues. The biggest problem was immediately clear, the instructions on how to structure the input files were not sufficient.

To rectify this, we changed our landing page to include explicit instructions on how to structure the files we need. The following page requires users to upload a staff members file, a projects file, and a students file. In the case where a developer wants to test the program, there is a button provided that will generate students and projects for them. This also includes a small rework of our random generation to remove the need for a staff members file. Our options page and solution page remained largely the same.

We also added an option to save solutions generated by the algorithms. This option appears as a button on the final solution page.

### Sprint 8:

This final sprint was a mish-mash of various tasks: cleaning up code from previous weeks, adding a few smaller features, and improving commenting across the board.

One significant optimisation we made was to our algorithms. Previously, we were using DAOs to retrieve student information, which were incredibly slow. We improved this by passing a list of all students and projects to the necessary functions, and accessing the list instead. This improved the speed of our simulated annealing algorithm massively, cutting the average runtime from 1-2 minutes down to less than 5 seconds.

We also added a visual indicator of the quality of our solution. We do this by adding a color to the list we pass to Thymeleaf, based on how far down in the student's preference list their assigned project is. This updates the background color of that students' row in the solution table. We also added a quality report to the bottom of the solution page, detailing how many students got their first preference, etc.

Unfortunately, because we only received a sample input file in this sprint, we spent a significant amount of time bolting on new parsing, as well as having to adjust our pre-existing systems, to accommodate this new filetype. We'd been trained to expect a master projects file at every turn up until this point, so the lack of one in the sample input file was quite the nuisance. It would have been nice to have had a sample input file from the beginning of the project, so we could shape our codebase accordingly.

We added a progress bar to the options page to display the current progress in the search for solutions. Unfortunately, we couldn't get this to work since the algorithm was being run on the server side of things. Despite many hours spent trawling StackOverflow and other sites, we couldn't find a definitive method for updating values after loading the page. The best we could find was AJAX, but nothing specific to Spring Boot. As such, we just left the progress bar to increment every so often.

## Team Report:

We are happy to split the credit for this project evenly, with each member receiving 33% of the credit.

In terms of how the work was divided, Jack and Rohan handled the Java side of things, working collaboratively on the algorithms etc. Monika handled the webpage side of things, creating the UI and doing testing to make sure everything in the backend was working.

As a team, we had a meeting on Monday or Tuesday every week to establish exactly what we needed to get done for that week's sprint. These meetings were usually very productive, everyone had ideas on how to improve upon what we had done previously, or how best to implement a new feature. When necessary, we also met later on in the week to discuss any issues we were having, and again, these proved to be quite fruitful.

# How To Run

The project folder includes a prepackaged jar for you to run in the "target" folder. To run it, you have two options.

You can simply double click on it, as you would any executable and it will run quietly in the background. The trouble with this method is that to stop the program you have to go to Task Manager and find the "OpenJDK Platform binary" process and kill it from there.

The method we would suggest is to run it by opening a command prompt, navigating to the folder in which the jar is contained, and typing "java -jar projectallocation-0.0.1-SNAPSHOT.jar". This will open the console for the program in the command prompt window, and allow it to be stopped at any time with a simple Ctrl+C.

If you have made some changes to the code, and wish to recompile it, use Maven to package it. Open command prompt and navigate to the root directory of the project, then type "mvn package" and let Maven do the rest. From there, just follow the previous steps to run it.