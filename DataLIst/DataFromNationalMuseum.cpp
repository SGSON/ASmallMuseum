#include <iostream>
#include <sstream>
#include <fstream>
#include <string>
#using <System.dll>


using namespace System;
using namespace System::Net;
using namespace System::Ne::Http;
using namespace System::IO;
using namespace std;

int main(int argc, const char * argv[]){
    string url = "http://www.emuseum.go.kr/openapi/code"; // URL
    url += "?ServiceKey=" + "Zty42NM%2FUj%2FwcSEKa%2BXBrpzXmGZYvsSid5GZlO1TxrsN9Zn%2F%2B5ZPlc6z3B7sqoBmdmCOFr5JJQi5AZ29VjF2mw%3D%3D"; // Service Key
    url += "&serviceKey=-";
    url += "&pageNo=1";
    url += "&numOfRows=10";
    url += "&parentCode=PS01";
    
    var request = (HttpWebRequest)WebRequest.Create(url);
        request.Method = "GET";
    
    string results = string.Empty;
    HttpWebResponse response;
    using (response = request.GetResponse() as HttpWebResponse)
    {
        StreamReader reader = new StreamReader(response.GetResponseStream());
        results = reader.ReadToEnd();
    }
    
    Console.WriteLine(results);

    
    return 0;
}