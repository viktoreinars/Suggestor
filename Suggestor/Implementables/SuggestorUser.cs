using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Suggestor
{
    public interface SuggestorUser
    {
        string Id { get; set; }
        Dictionary<string, SuggestorCollection> Collections { get; set; }
        /*
        string GetId();
        Dictionary<string, SuggestorCollection> GetCollections();

        void SetId(string userId);
        void SetCollections(Dictionary<string, SuggestorCollection> collections);
         */
    }
}
