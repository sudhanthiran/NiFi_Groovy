

def palindrome

String a ="abca acba"
String left =""
String Right = ""
len = a.length()
int middle = len/2
println(len)
if (len%2 == 0)
{

  left = a.substring(0,middle)
    right = a.substring(middle,len)
}
else {
  left = a.substring(0,middle)
    right = a.substring(middle+1,len)
}

for (i in left)
{
  print(i)
}