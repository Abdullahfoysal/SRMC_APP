#include<bits/stdc++.h>

using namespace std;

#define size1 100000
char temp[size1];

char* deletefunction(char main_str[],int pos,int length)
{

    int j=0;
    for(int i=0;i<strlen(main_str);i++)
    {
        if(i==pos)i+=length;

           temp[j]=main_str[i];
           j++;


    }
    temp[j]='\0';

    return temp;

}



int main()
{
   char  str_input[size1];
   int pos,length;

    cin>>str_input;
    cin>>pos>>length;


    deletefunction(str_input,pos,length);
    printf("%s",temp);


return 0;
}
