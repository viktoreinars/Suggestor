using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Suggestor
{
    public interface SuggestorItem
    {
        string Id { get; set; }
        string Description { get; set; }
        int Quantity { get; set; }
        /*
        string GetId();
        string GetDescription();
        int GetQuantity();
        void SetId(string id);
        void SetDescription(string description);
        void SetQuantity(int quantity);
         */
        SuggestorItem DeepCopy();
    }
}
