#include<bits/stdc++.h>

using namespace std;

#define size1 100000
char temp2[size1];

char* updatefunction(char main_str[],char update_str[],char update_with[] )
{
    int c=0,k=0;
    for(int i=0;i<strlen(main_str);i++)
    {

        if(main_str[i]==update_str[0])
        {
           for(int j=0;j<strlen(update_str);j++)
           if(main_str[i+j]==update_str[j]) {c++;}

             if(c==strlen(update_str))
             {

                for(int m=0;m<strlen(update_with);m++)
               {
                  temp2[k]=update_with[m];
                  k++;

               }
               i+=c-1;
            }




        }
        else
        {
            c=0;
            temp2[k]=main_str[i];

            k++;

        }



    }
    temp2[k]='\0';

    return temp2;


}


int main()
{
 char main_str[size1],update_str[size1],update_with[size1];

   scanf("%[^\n]s",main_str);
   getchar();
   scanf("%[^\n]s",update_str);
   getchar();
   scanf("%[^\n]s",update_with);

   updatefunction(main_str,update_str,update_with);
   printf("%s",temp2);

   return 0;




}
