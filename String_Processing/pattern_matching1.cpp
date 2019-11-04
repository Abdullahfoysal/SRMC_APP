#include<bits/stdc++.h>

using namespace std;

int pattern_matching(char main_str[],char match_pattern[])
{
    int d=0;
    for(int i=0;i<strlen(main_str);i++)
    {
        for(int j=0,c=0;j<strlen(match_pattern);j++)
        {
            if(main_str[i+j]==match_pattern[j])c++;
            if(c==strlen(match_pattern)){d++;c=0;}
        }
    }
    return d;


}

int main()
{
    char main_str[]="i love my bangladesh,i really like bangladesh";
    char match_pattern[]="bangladesh";

    cout<<"totall match found "<<pattern_matching(main_str,match_pattern);



return 0;
}
