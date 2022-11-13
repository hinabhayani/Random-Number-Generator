# Random-Number-Generator

## External Merge Sort

```
Generate any random numbers and output those numbers to a file.
Sort output of random numbergenerator and result into another file.
 ```
This project contains program file that generate random numbers between provided range. 

I have used External Merge Sort Algorithm to sort large amount of data. It enalyses the data and determines automatically if algorithm process will be handled by system memory or not.
The following case has been carried out based on capacity of system. Let's say, we have large amount of data then it will be devided into small chunks/temporary files.
By doing so, system can be kept runnning smoothly and we wont have issue of heap space/out of memory.

On the other hand, if External Merge Sort Algorithm determines that the process will be handled by memory/system then it will not bother to devided into small chunks and will keep running smoothly.
