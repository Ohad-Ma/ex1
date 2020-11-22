# ![GitHub Logo](https://imgur.com/OppFphQ.png) 


## About
This project represent methods that can be done on an **undirected weighted graph**,  
such as : 
- _Add\remove a vertex on the graph._ 
- _Add an edge by connecting two vertices._ 
- _Check whether vertices has an edge between them or not._  
- _Check whether the graph is connected or not._ 
- _Find the **shortest path** and its **distance** between two vertices by using Dijkstra Algorithm._
- _And much more.._
   

## Classes
 * **WGraph_DS**- A class that represent an unidirectional weighted graph data structure.
Due to time complexity (mostly O(1)) and the quick draw of data the class contains three maps, each represents another piece of data and store each of the *__inner classes__ info. The class has all the methods to create and manipulate a graph.  
 *__Inner classes__: 
    * **vertex** - An inner class that represents a vertex on a graph. A vertex Can be added, manipulated or removed. 
    * **edge** - An inner class that represents an edge on a graph by getting two objects vertex type and since it's a weighted graph then a weight as well which can be manipulated in the outer class WGraph_DS.
 

- **WGraph_Algo**- A class that represents a Dijkstra graph theory algorithm that can be done on a directed and undirected graph. The data structure I decided to use for the algorithm class is MinHeap due the need of priority queue and its efficiency with graphs helping us knowing the minimum and maximum easily (time complexity of O(1)).

## Contributing

* [MIT - 6-006-introduction-to-algorithms-fall-2011/](https://ocw.mit.edu/courses/electrical-engineering-and-computer-science/6-006-introduction-to-algorithms-fall-2011/lecture-videos/MIT6_006F11_lec16.pdf) - for showing me another point of view in Dijkstra algorithm

* [core-dijkstras-algorithm](https://www.coursera.org/lecture/advanced-data-structures/core-dijkstras-algorithm-2ctyF) - for showing how the algorithm works

* [photopea](https://www.photopea.com/) - online photoshop that aided me with the logo

* [features/mastering-markdown](https://guides.github.com/features/mastering-markdown/) - for the syntax in this README

* [Justrygh](https://github.com/Justrygh) - for guiding me in this project
* [benmoshe](https://github.com/benmoshe) - for guiding me in this project
* [simon-pikalov](https://github.com/simon-pikalov) - for guiding me in this project




## License
[MIT](https://choosealicense.com/licenses/mit/)