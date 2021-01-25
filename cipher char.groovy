String a = "aZ"

cj = 40

for (i in a)
{
    char b = (int)i
    jump = cj
    for(int j=0;jump>26;i++){
        jump = cj % 26
    }

    if((int) i >=97 && (int) i <= 122){
        b= (int) b + jump
        if((int)b > 122)
        {
            b = (int)b - 26
        }

    }
    if((int) i >=65 && (int) i <= 91){
        b= (int) b + jump
        if((int)b > 91)
        {
            b = (int)b - 26
        }

    }
    println(b)
}
