using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Suggestor
{
    public interface SuggestorItem
    {
        string GetId();
        string GetDescription();
        int GetQuantity();
        void SetId(string id);
        void SetDescription(string description);
        void SetQuantity(int quantity);
        SuggestorItem DeepCopy();
    }
}
