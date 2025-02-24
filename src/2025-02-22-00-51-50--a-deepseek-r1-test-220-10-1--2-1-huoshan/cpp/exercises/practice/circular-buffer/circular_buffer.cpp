#include "circular_buffer.h"

namespace circular_buffer {


// Explicit template instantiation for common types
template class circular_buffer<int>;
template class circular_buffer<double>;
template class circular_buffer<char>;

}  // namespace circular_buffer
