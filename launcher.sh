

#
# Root directory of your project
PROJDIR=$HOME/AOS/Project1

#
# This assumes your config file is named "config.txt"
# and is located in your project directory
#
CONFIG=$PROJDIR/config.txt

#
# Directory your java classes are in
#
BINDIR=$PROJDIR

#
# Your main project class
#
PROG=Project1


cat $CONFIG | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    #echo $i    
    netId=$( echo $i | awk '{ print $2 }' )
    totalNodes=$( echo $i | awk '{ print $1 }' )
    #echo $netId	
    
    for ((a=1; a <= $totalNodes ; a++))
    do
    	read line 
		#echo $line
		nodeId=$( echo $line | awk '{ print $1 }' )
       	host=$( echo $line | awk '{ print $2 }' )
		host="$host.utdallas.edu"
		#$echo $nodeId
		#echo $host
	
		ssh -l "$netId" "$host" "cd $BINDIR;java $PROG $nodeId" &
	
    done
   
)


