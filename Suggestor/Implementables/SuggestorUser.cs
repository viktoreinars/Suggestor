using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Suggestor
{
    public interface SuggestorUser
    {
        string Id { get; set; }
        //Dictionary<string, SuggestorCollection> Collections { get; set; }
        List<SuggestorCollection> Collections { get; set; }
    }
}
