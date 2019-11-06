#include<bits/stdc++.h>

using namespace std;
#define size1 100000

int search_word(char main_str[],char searching[])
{

    for(int i=0,c=0;i<strlen(main_str);i++)
    {
        for(int j=0;j<strlen(searching);j++)
        {
            if(main_str[i+j]==searching[j])c++;
        }
        if(c==strlen(searching)) return i;
    }
    return 0;
}

int main()
{
    char main_str[size1],searching[size1];
    scanf("%[^\n]s",main_str);
    getchar();
    scanf("%[^\n]s",searching);

    if(search_word(main_str,searching)>0)printf("-%s- found on index:%d the text",searching,search_word(main_str,searching));
        else printf("-%s- not found on the text",searching);




}
